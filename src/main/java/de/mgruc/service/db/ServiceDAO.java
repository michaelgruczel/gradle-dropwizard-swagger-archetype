package de.mgruc.service.db;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlCall;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;

public interface ServiceDAO {

  @SqlUpdate("create table IF NOT EXISTS entry (id int primary key, value varchar(100))")
  void createEntryTable();

  @SqlUpdate("insert into entry (id, value) values (:id, :value)")
  void insert(@Bind("id") int id, @Bind("value") String value);

  @SqlQuery("select value from entry where id = :id")
  String findValueById(@Bind("id") long id);

  @SqlCall("delete from entry")
  void deleteAll();

}
