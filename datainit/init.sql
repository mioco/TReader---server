create table tag (
  id  int primary key auto_increment,
  tag varchar(255) not null,
  unique key uk_tag(tag)
)
  engine = Innodb
  default charset = UTF8
  comment 'tag';

create table url (
  id        int primary key auto_increment,
  url       varchar(255) not null,
  domain    varchar(255) not null,
  temp_item varchar(128) null,
  unique key uk_url(url)
)
  engine = Innodb
  default charset = UTF8
  comment 'url';

create table user (
  id       int auto_increment primary key,
  name     varchar(32)  null,
  email    varchar(64)  not null,
  password varchar(128) not null,
  avatar   varchar(128) null,
  role     varchar(64)  null,
  ksid     varchar(64)  not null,
  unique key uk_email(email)
)
  engine = Innodb
  default charset = UTF8
  comment 'user';

create table user_tag (
  id      int auto_increment primary key,
  tag_id  int not null,
  user_id int not null,
  unique uk_tag_user(tag_id, user_id),
  index id_tag(tag_id),
  index id_user(user_id)
)
  engine = Innodb
  default charset = UTF8
  comment 'user_tag';

create table tag_url (
  id     int auto_increment primary key,
  tag_id int not null,
  url_id int not null,
  unique uk_tag_url(tag_id, url_id),
  index id_tag(tag_id),
  index id_url(url_id)
)
  engine = Innodb
  default charset = UTF8
  comment 'user_tag';

create table user_url (
  id      int primary key auto_increment,
  user_id int not null,
  url_id  int not null,
  unique uk_user_url(user_id, url_id),
  index id_user(user_id),
  index id_url(url_id)
)
  engine = Innodb
  default charset = UTF8
  comment 'user_url';

create table webpage (
  id   int primary key auto_increment,
  url_id int not null ,
  html longtext ,
  text text,
  url  varchar(255),
  seen datetime,
  unique key uk_url(url)
)
  engine = Innodb
  default charset = UTF8
  comment 'webpage';

create table webpage_tag (
  id int primary key auto_increment,
  webpage_id int not null,
  tag_id int not null,
  unique key uk_webpage_tag(webpage_id, tag_id)
)
  engine = Innodb
  default charset = UTF8
  comment 'url和tag和webpage的关联关系';

