package me.alan.reconciliationtransactionsfinancieres.config;

import lombok.extern.slf4j.Slf4j;
import me.alan.reconciliationtransactionsfinancieres.mapper.TransactionMapper;
import me.alan.reconciliationtransactionsfinancieres.model.dto.TransactionDto;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionEntity;
import me.alan.reconciliationtransactionsfinancieres.model.entity.TransactionErrors;
import me.alan.reconciliationtransactionsfinancieres.repository.TransactionRepository;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.json.JacksonJsonObjectReader;
import org.springframework.batch.infrastructure.item.json.JsonItemReader;
import org.springframework.batch.infrastructure.item.json.builder.JsonItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;

@Slf4j
@Configuration
@EnableBatchProcessing
public class BatchTransactionConfig {

    private static final String VALID_TRANSACTION_DATE_TIME_REGEX = "^\\d{4}-(0[1-9]|1[0-2])-(0[1-9]|[12][0-9]|3[01])T([01][0-9]|2[0-3]):([0-5][0-9])(?::([0-5][0-9]))?$";
    private final TransactionRepository repository;
    private final JobRepository jobRepository;

    public BatchTransactionConfig(final JobRepository jobRepository, final TransactionRepository repository, MongoTemplate mongoTemplate) {
        this.jobRepository = jobRepository;
        this.repository = repository;
    }

    @Bean
	public Job job(final Step importTransactionsStep) {
		return new JobBuilder("importTransactionsJob", jobRepository)
                .start(importTransactionsStep)
				.build();
	}

    @Bean
    public Step importTransactionsStep() {
        return new StepBuilder(jobRepository)
                .<TransactionDto, TransactionEntity>chunk(1000)
                .reader(jsonItemReader())
                .processor(transactionDto -> {
                    TransactionEntity transactionEntity = TransactionMapper.INSTANCE.transactionDtoToTransactionEntity(transactionDto);

                    if (!transactionEntity.getDate().matches(VALID_TRANSACTION_DATE_TIME_REGEX)) {
                        transactionEntity.getTransactionErrors().add(TransactionErrors.INVALID_DATE_TIME);
                    }
                    return transactionEntity;
                })
                .writer(repositoryItemWriter())
                .build();
    }

    @Bean
    public JsonItemReader<TransactionDto> jsonItemReader() {
        return new JsonItemReaderBuilder<TransactionDto>()
                .name("transactionJsonReader")
                .resource(new ClassPathResource("transactions.json"))
                .jsonObjectReader(new JacksonJsonObjectReader<>(TransactionDto.class))
                .build();
    }

    @Bean
    public ItemWriter<TransactionEntity> repositoryItemWriter() {
        return items -> items.forEach(repository::save);
    }
}
