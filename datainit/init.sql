create table tag (
  id  int primary key auto_increment,
  tag varchar(255) not null ,
  unique key uk_tag(tag)
)
  engine = Innodb
  default charset = UTF8
  comment 'tag';

create table url (
  id        int primary key auto_increment,
  url       varchar(255) null,
  temp_item varchar(128) null,
  unique key uk_url(url)
)
  engine = Innodb
  default charset = UTF8
  comment 'url';

create table user (
  id       int auto_increment primary key,
  name     varchar(32)  not null,
  email    varchar(64)  not null,
  password varchar(128) not null,
  avatar   varchar(128) not null,
  role     varchar(64)  not null,
  ksid     varchar(64)  not null,
  unique key uk_name(name),
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




