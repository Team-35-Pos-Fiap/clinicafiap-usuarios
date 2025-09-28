create table perfil (
	id int not null auto_increment,
	nome varchar(150) null,
	primary key (id)
);

create table usuario (
	id uuid not null,
    nome varchar(150) not null,
    email varchar(50) not null,
    senha varchar(255) not null,
    ativo tinyint not null default 1,
    id_perfil int not null,
    primary key (id),
    foreign key (id_perfil) references perfil (id)
);