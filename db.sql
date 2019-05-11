drop table likes;
drop table comments;
drop table photos;
drop table locations;
drop table followings;
drop table users;

create table users(
    userID int primary key generated always as identity,
    userName varchar(30) NOT NULL UNIQUE,
    firstName varchar(30) NOT NULL,
    lastName varchar(30) NOT NULL,
    password varchar(150) NOT NULL,
    ts timestamp DEFAULT CURRENT_TIMESTAMP not null,
    privacy boolean NOT NULL
);
create table locations(
    locationID int primary key generated always as identity,
    locationName varchar(100) not null unique
);
create table photos(
    photoID int primary key generated always as identity,
    photoSrc VARCHAR(150) NOT NULL,
    caption varchar(50) NOT NULL,
    price real not null,
    locationID int not null,
    userID int NOT NULL,
    ts timestamp DEFAULT CURRENT_TIMESTAMP not null,
    constraint fk_photos_users foreign key(userID) references users(userID) on DELETE CASCADE,
    constraint fk_photos_locations foreign key(locationID) references locations(locationID) on DELETE CASCADE
);
create table comments(
    commentID int primary key generated always as identity,
    photoID int NOT NULL,
    userID int NOT NULL,
    text varchar(100) NOT NULL,
    ts timestamp DEFAULT CURRENT_TIMESTAMP not null,
    constraint fk_comments_users foreign key(userID) references users(userID) on DELETE CASCADE,
    constraint fk_comments_photos foreign key(photoID) references photos(photoID) on DELETE CASCADE
);
create table likes(
    photoID int NOT NULL,
    userID int NOT NULL,
    constraint fk_likes_users foreign key(userID) references users(userID) on DELETE CASCADE,
    constraint fk_likes_photos foreign key(photoID) references photos(photoID) on DELETE CASCADE,
    PRIMARY key(photoID, userID)
);
create table followings(
    followerUserID int NOT NULL,
    followingUserID int NOT NULL,
    constraint fk_followings_1 foreign key(followerUserID) references users(userID) on DELETE CASCADE,
    constraint fk_followings_2 foreign key(followingUserID) references users(userID) on DELETE CASCADE,
    PRIMARY key(followerUserID, followingUserID)
);
