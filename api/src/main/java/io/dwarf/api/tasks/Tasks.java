package io.dwarf.api.tasks;

import io.dwarf.api.links.LinksRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Tasks {
    @Autowired
    LinksRepo linksRepo;

    @Scheduled(fixedRateString = "${app.links.cleaner.interval}")
    public void cleanGoneLinks() {
        System.out.println("Run cleaner");
        linksRepo.cleanup();
    }
}