Create Table Yelp_User(
	user_id varchar2(100),
	yelping_since Date,
	review_count Integer,
	name varchar2(100),
	average_stars Real,
	Primary Key(user_id));


Create Table User_Friends(
	User_Id varchar2(100),
	friends varchar2(100),
	Foreign Key(User_Id) references Yelp_User(User_Id));

Create Table Business(
	business_id varchar(100),
	full_address varchar(500),
	city varchar(50),
	state varchar(50),
	review_count integer,
	name varchar(100),
	stars real,
	Primary Key(business_id));

Create table Review(
	useful integer,
	funny integer,
	cool integer,
	user_id varchar2(100),
	review_id varchar2(100),
	stars integer,
	rdate DATE,
        text varchar2(400),
	business_id varchar(100),
	Primary Key(review_id),
	Foreign Key(user_id) references Yelp_User(user_id),
	Foreign Key(business_id) references Business(business_id));


Create table BCategory(
	business_id varchar(100),
  	categories varchar(100),
  	Foreign Key(business_id) References Business(business_id));

Create Table BSubCategory(
	business_id varchar(100),
	categories varchar(100),
	subcategories varchar(100),
	Foreign Key(business_id) references Business(business_id));

create table CheckedIn( 
	Business_Id varchar(100), 
	Day Integer , 
	Hour Integer , 
	checkInNum Integer , 
	Foreign Key(Business_Id) references Business(business_id));