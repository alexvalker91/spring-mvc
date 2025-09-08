INSERT INTO event_db (event_title, event_date, event_ticket_price) VALUES ('My First Event', CURRENT_TIMESTAMP, 100);
INSERT INTO event_db (event_title, event_date, event_ticket_price) VALUES ('Another Event', CURRENT_TIMESTAMP, 50);

INSERT INTO user_db (user_name, user_email) VALUES ('Alice', 'alice@example.com');
INSERT INTO user_db (user_name, user_email) VALUES ('Bob', 'bob@example.com');

INSERT INTO user_account_db (user_id, user_amount) VALUES (1, 200);
INSERT INTO user_account_db (user_id, user_amount) VALUES (2, 10);

INSERT INTO ticket_db (event_id, user_id, category, ticket_place) VALUES (1, 1, 'STANDARD', 1);
