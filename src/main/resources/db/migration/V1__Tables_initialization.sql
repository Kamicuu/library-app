/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  Kamil
 * Created: 25 maj 2022
 */
-- H2 2.1.212; 
-- Book table      
CREATE CACHED TABLE "LIBRARYDB"."BOOK"(
    "ID" CHARACTER VARYING(100) NOT NULL,
    "AUTHOR" CHARACTER VARYING(255),
    "CAN_BE_BORROWED" BOOLEAN,
    "DESCRIPTION" CHARACTER VARYING(500),
    "IMAGE_URL" CHARACTER VARYING(255),
    "ISBN" CHARACTER VARYING(100),
    "LANGUAGE" CHARACTER VARYING(100),
    "PUBLICATION_DATE" DATE,
    "PUBLISHING_HOUSE" CHARACTER VARYING(255),
    "TITLE" CHARACTER VARYING(255)
);   
ALTER TABLE "LIBRARYDB"."BOOK" ADD CONSTRAINT "LIBRARYDB"."PK_BOOK" PRIMARY KEY("ID");         

-- Borrowing table
CREATE CACHED TABLE "LIBRARYDB"."BORROWING"(
    "ID" INTEGER NOT NULL,
    "BORROWING_DATE" TIMESTAMP,
    "RETURNING_DATE" TIMESTAMP
);    
ALTER TABLE "LIBRARYDB"."BORROWING" ADD CONSTRAINT "LIBRARYDB"."PK_BORROWING" PRIMARY KEY("ID");    

-- Connection table borrowing - book       
CREATE CACHED TABLE "LIBRARYDB"."BORROWING_BOOK"(
    "ID_BORROWING" INTEGER NOT NULL,
    "ID_BOOK" CHARACTER VARYING(255) NOT NULL
);       
ALTER TABLE "LIBRARYDB"."BORROWING_BOOK" ADD CONSTRAINT "LIBRARYDB"."PK_BORROWING_BOOK" PRIMARY KEY("ID_BORROWING", "ID_BOOK");          

-- User details table             
CREATE CACHED TABLE "LIBRARYDB"."USER_DETAILS"(
    "USER_ID" INTEGER NOT NULL,
    "CITY" CHARACTER VARYING(100),
    "CREATED_AT" TIMESTAMP,
    "FIRST_NAME" CHARACTER VARYING(100),
    "LAST_NAME" CHARACTER VARYING(100),
    "NUMBER" CHARACTER VARYING(15),
    "POST_CODE" CHARACTER VARYING(6),
    "STREET" CHARACTER VARYING(100),
    "UPDATED_AT" TIMESTAMP
);           
ALTER TABLE "LIBRARYDB"."USER_DETAILS" ADD CONSTRAINT "LIBRARYDB"."PK_USER_DETAILS" PRIMARY KEY("USER_ID");            

-- User table        
CREATE CACHED TABLE "LIBRARYDB"."USER_LOGIN"(
    "ID" INTEGER NOT NULL,
    "LOGIN" CHARACTER VARYING(45),
    "EMAIL" CHARACTER VARYING(100),
    "PASSWORD" CHARACTER VARYING(255),
    "ROLE" CHARACTER VARYING(255)
);     
ALTER TABLE "LIBRARYDB"."USER_LOGIN" ADD CONSTRAINT "LIBRARYDB"."PK_USER_LOGIN" PRIMARY KEY("ID");             
ALTER TABLE "LIBRARYDB"."USER_DETAILS" ADD CONSTRAINT "LIBRARYDB"."FK_ID_USER" FOREIGN KEY("USER_ID") REFERENCES "LIBRARYDB"."USER_LOGIN"("ID") NOCHECK;              
ALTER TABLE "LIBRARYDB"."BORROWING_BOOK" ADD CONSTRAINT "LIBRARYDB"."FK_ID_BORROWING" FOREIGN KEY("ID_BORROWING") REFERENCES "LIBRARYDB"."BORROWING"("ID") NOCHECK;        
ALTER TABLE "LIBRARYDB"."BORROWING_BOOK" ADD CONSTRAINT "LIBRARYDB"."FK_ID_BOOK" FOREIGN KEY("ID_BOOK") REFERENCES "LIBRARYDB"."BOOK"("ID") NOCHECK;  

-- Create sequence for hibernate
CREATE SEQUENCE "LIBRARYDB"."HIBERNATE_SEQUENCE"
