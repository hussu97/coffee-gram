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
    status varchar(50),
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
    constraint fk_photos_users foreign key(userID) references users(userID) on DELETE CASCADE
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

insert into locations (locationname) values ('Byblos Hospitality Group Al Mina Road - Dubai');
insert into locations (locationname) values ('Capital Club Dubai Gate Village, Building 3 - Building #3');
insert into locations (locationname) values ('Pharaoh Cafe & Restaurant Arabian Courtyard Hotel & Spa');
insert into locations (locationname) values ('Baker & Spice Souk Al Bahar, Downtown Dubai');
insert into locations (locationname) values ('Basta Art Cafe 63 Al Faheide - Dubai');
insert into locations (locationname) values ('PAK AL REYAN RESTAURANT S01, 2 & 4 W1, RUSSIA CLUSTER،');
insert into locations (locationname) values ('Marks and Spencer Ground & 1st Floor, Festival Centre - 7976 Crescent Drive - دبي');
insert into locations (locationname) values ('Shakespeare and Co. Ground Floor, Al Saqr Business Tower, Sheikh Zayed Road');
insert into locations (locationname) values ('Tche Tche Emaar Lofts Buidling, #3 - Emaar Boulevard - Dubai');
insert into locations (locationname) values ('Snooker World Al Ittihad Road - Dubai');
insert into locations (locationname) values ('French Bakery Financial Centre Road,Al Safa,Behind Shangrila Hotel');
insert into locations (locationname) values ('Starbucks Terminal 2 Departure Area، Beside Duty Free');
insert into locations (locationname) values ('Starbucks Movenpick Hotel & Apartment, 19th Street, Oud Metha');
insert into locations (locationname) values ('Carluccios Second floor, Southeast Wing,Mirdif City Center - دبي');
insert into locations (locationname) values ('French Bakery Al Razzi Building 59, Opposite Sulaiman Al-Habib hospital, Dubai Health Care City');
insert into locations (locationname) values ('Lauberge Cafe 128 Al Nahda Street - Sharjah');
insert into locations (locationname) values ('Local House Restaurant 51, Al Bastakiya, Near Al Fahidi Round About Opposite Al Mussalla Post Office Meena Bazaar');
insert into locations (locationname) values ('BAKEWELL BAKERY Al Rolla Road - Dubai');
insert into locations (locationname) values ('Tim Hortons Jumeira Centre - 65 Jumeirah Street - Dubai');
insert into locations (locationname) values ('Starbucks Burj Dubai Down Town, Level 3 -دبي');
insert into locations (locationname) values ('Hotel Novotel Dubai Deira City Centre 8th street, Port Saeed District, Front of Deira City CentreMall');
insert into locations (locationname) values ('Premier Inn Dubai International Airport Hotel Opp. Terminal 3, Dubai International Airport');
insert into locations (locationname) values ('Arabian Courtyard Hotel & Spa Al Seef Area Opposite Dubai Museum, شارع الفهيدي');
insert into locations (locationname) values ('OYO 117 St George Hotel 10 Baniyas Street,Dubai Creek,Al Ras,Deira - Behind Public Library');
insert into locations (locationname) values ('Fairmont Dubai Sheikh Zayed Road');
insert into locations (locationname) values ('Flora Creek Deluxe Hotel Apartments Port Saeed Road ,Near Deira City Centre Dubai Creek');
insert into locations (locationname) values ('Mercure Gold Hotel Al Mina Road Dubai Al Mina Road,Al Mina');
insert into locations (locationname) values ('Conrad Dubai Sheikh Zayed Road - Dubai');
insert into locations (locationname) values ('Jood Palace Hotel Dubai 36-A Street Off Al Rigga Road, Deira, Near Al Rigga Metro Station');
insert into locations (locationname) values ('Sandras Inn Hotel Near Baniyas Square Metro Station');
insert into locations (locationname) values ('Hotel Raffles Dubai Wafi - Sheikh Rashid Road');insert into locations (locationname) values ('Carlton Downtown Hotel Sheikh Zayed Road, Near Financial Centre Metro Station');
insert into locations (locationname) values ('The Canvas Hotel Dubai MGallery By Sofitel 23 Kuwait Street - Dubai');
insert into locations (locationname) values ('Byblos Hospitality Group Al Mina Road - Dubai');
insert into locations (locationname) values ('Hard Rock Cafe Dubai Festival City Zone 8A, Dubai Festival City Mall Next to Marks & Spencer');insert into locations (locationname) values ('The Oberoi, Dubai The Oberoi Centre - Al Aamal Street - Dubai');
insert into locations (locationname) values ('Sofitel Dubai Downtown Sheikh Zayed Road - Dubai');insert into locations (locationname) values ('Zuma Restaurant Gate Village 06, Podium Level - Al Saada Street - Dubai');
insert into locations (locationname) values ('La Maison Al Abraj Street, Business Bay');insert into locations (locationname) values ('Capital Club Dubai Gate Village, Building 3 - Building #3');