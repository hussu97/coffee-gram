drop table likes;
drop table comments;
drop table posts;
drop table followings;
drop table users;

create table users (
	userID int primary key generated always as identity,
	userName varchar (30) NOT NULL UNIQUE,
        firstName varchar (30) NOT NULL,
        lastName varchar (30) NOT NULL,
        password varchar (30) NOT NULL,
        privacy boolean NOT NULL
);
create table photos (
	photoID int primary key generated always as identity,
	photoSrc VARCHAR (150) NOT NULL,
	caption varchar (50) NOT NULL,
    price real not null,
    location varchar (100),
        userID int NOT NULL,
	constraint fk_photos_users foreign key (userID)
		references users (userID)
);
create table comments (
        commentID int primary key generated always as identity,
        photoID int NOT NULL,
        userID int NOT NULL,
        text varchar (100) NOT NULL,
        constraint fk_comments_users foreign key (userID)
		references users (userID),
        constraint fk_comments_photos foreign key (photoID)
		references photos (photoID)
);
create table likes (
        photoID int NOT NULL,
        userID int NOT NULL,
        constraint fk_likes_users foreign key (userID)
		references users (userID),
        constraint fk_likes_photos foreign key (photoID)
		references photos (photoID)
);
create table followings (
	followerUserID int NOT NULL,
	followingUserID int NOT NULL,
	constraint fk_followings_1 foreign key (followerUserID)
		references users (userID), 
	constraint fk_followings_2 foreign key (followingUserID)
		references users (userID)
);
