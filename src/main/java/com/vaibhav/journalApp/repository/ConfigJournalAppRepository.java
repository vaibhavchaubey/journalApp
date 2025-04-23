package com.vaibhav.journalApp.repository;

import com.vaibhav.journalApp.entity.ConfigJournalAppEntity;
import com.vaibhav.journalApp.entity.JournalEntry;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigJournalAppRepository extends MongoRepository<ConfigJournalAppEntity, ObjectId> {


}
