package me.alan.reconciliationtransactionsfinancieres.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.alan.reconciliationtransactionsfinancieres.repository.TransactionRepository;
import me.alan.reconciliationtransactionsfinancieres.service.StepReferenceService;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoadTransactionsIntoDatabaseRunner implements CommandLineRunner {

    private final JobOperator jobOperator;
    private final Job job;
    private final TransactionRepository repository;
    private final StepReferenceService stepReferenceService;

    @Override
    public void run(final String... args) throws Exception {
        if (repository.count() == 0) {
            jobOperator.start(job, new JobParameters());
            stepReferenceService.updateStepRanksAndEventRanksForTransactions();
        }
    }
}
