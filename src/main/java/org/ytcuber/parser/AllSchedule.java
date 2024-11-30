package org.ytcuber.parser;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.ytcuber.initialization.Initialization;
import org.ytcuber.database.repository.GroupRepository;
import java.io.IOException;

@Component
public class AllSchedule {
    private Initialization initialization;
    private GroupProcessor groupProcessor;
    private GroupRepository groupRepository;
    @Autowired
    public void ApplicationInitializer(GroupProcessor groupProcessor, Initialization initialization, GroupRepository groupRepository) {
        this.initialization = initialization;
        this.groupProcessor = groupProcessor;
        this.groupRepository = groupRepository;
    }

    @PostConstruct
    public void init() throws IOException, InterruptedException {
        for (int i = 1; i <= 4; i++) { groupProcessor.processGroups(String.valueOf(i)); }
        Integer lastId = groupRepository.findLastId();
        for (int i = 1; i <= lastId; i++) {
            String groupName = String.valueOf(groupRepository.findNameById(i));
            initialization.processExcelParse(groupName);
        }
//        Нет субботы у 25, 54, 67, Странная фигня у КС-23-1
//        String groupName = String.valueOf(groupRepository.findNameById(38)); initialization.processExcelParse(groupName);
    }
}
