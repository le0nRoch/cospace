# Cospace Platform

Cospace is a platform where people can request a space in a coworking space in zurich. \
Admins then can approve or decline the request.

## Setup

- Clone the repository
- Open the project in your IDE
- Install the maven dependencies

## Run

- Run the docker-compose file in the root directory
- Run the application
- Open the application in your browser at `http://localhost:8080`
- (Optional) Swagger UI is available at `http://localhost:8080/swagger`

## Test Data

The test data is loaded on startup in
the [CoworkingSpaceApplication](./src/main/java/ch/zli/cospace/LbCospaceApplication.java) class. \
This happens only if the `spring.profiles.active` property is set to `dev`.

### Users

| Email                           | Password | First Name | Last Name | Role   |
|---------------------------------|----------|------------|-----------|--------|
| first.user.gets.admin@gmail.com | admin123 | First      | AdminUser | ADMIN  |
| admin@gmail.com                 | admin123 | Admin      | Admin     | MEMBER |
| deleteme@gmail.com              | deleteme | Delete     | Me        | MEMBER |
| user@gmail.com                  | useruser | User       | User      | MEMBER |

### Bookings

| User Id | Date           | Timeslot | Status    | Description |
|---------|----------------|----------|-----------|-------------|
| 4       | {current date} | FULL_DAY | REQUESTED | {null}      |