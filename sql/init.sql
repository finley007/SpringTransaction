-- Create table
create table CLASS_STATISTIC
(
  SID   VARCHAR2(64) not null,
  CID   VARCHAR2(64) not null,
  SCORE VARCHAR2(64) not null
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table CLASS_STATISTIC
  add constraint PK_CLASS_STATISTIC primary key (SID);

  
insert into CLASS_STATISTIC (SID,CID,SCORE) 
 values('1','A','89');
insert into CLASS_STATISTIC (SID,CID,SCORE) 
 values('2','A','88');
insert into CLASS_STATISTIC (SID,CID,SCORE) 
 values('3','B','94');
insert into CLASS_STATISTIC (SID,CID,SCORE) 
 values('4','B','93');
commit;
  