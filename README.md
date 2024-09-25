This project implements a video streaming platform with basic user functionalities. The system allows users to register, upload, watch, and rate videos. Each user’s information, including name, email, age, gender, and location, is stored, and users can upload videos categorized into various groups. System that uses JDBC for communicating with the database in MS SQL.

Key functionalities include:
User Management: User details such as name, email, and location are stored.
Video Uploading: Users can upload videos, and for each video, details like title, duration, owner, and upload date are recorded.
Category Management: Videos can belong to multiple categories.
Subscription System: Users can subscribe to monthly packages with stored subscription details like start date and price.
Video Playback: Users can watch videos with information on the start time, watched duration, and progress being tracked.
Video Rating: Users can rate videos from 1 to 5, with the rating date and time being stored.
The system ensures that users cannot have overlapping subscriptions and that subscription prices are based on current package rates.
