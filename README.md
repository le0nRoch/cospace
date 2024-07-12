# Coworking Space

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

