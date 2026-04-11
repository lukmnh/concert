
CREATE TABLE users (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(100) NOT NULL,
    CONSTRAINT uq_users_email UNIQUE (email)
);

CREATE TABLE concerts (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(200) NOT NULL,
    venue       VARCHAR(200) NOT NULL,
    description TEXT,
    created_at  TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE TABLE ticket_slots (
    id                  BIGSERIAL PRIMARY KEY,
    concert_id          BIGINT      NOT NULL,
    sale_start          TIMESTAMP   NOT NULL,
    sale_end            TIMESTAMP   NOT NULL,
    total_tickets       INT         NOT NULL CHECK (total_tickets > 0),
    remaining_tickets   INT         NOT NULL CHECK (remaining_tickets >= 0),
    version             BIGINT      NOT NULL DEFAULT 0,
    CONSTRAINT fk_slot_concert FOREIGN KEY (concert_id) REFERENCES concerts (id),
    CONSTRAINT chk_slot_dates  CHECK (sale_end > sale_start),
    CONSTRAINT chk_remaining   CHECK (remaining_tickets <= total_tickets)
);

CREATE TABLE bookings (
    id         BIGSERIAL PRIMARY KEY,
    slot_id    BIGINT    NOT NULL,
    user_id    BIGINT    NOT NULL,
    quantity   INT       NOT NULL CHECK (quantity > 0),
    booked_at  TIMESTAMP NOT NULL DEFAULT NOW(),
    CONSTRAINT fk_booking_slot FOREIGN KEY (slot_id) REFERENCES ticket_slots (id),
    CONSTRAINT fk_booking_user FOREIGN KEY (user_id) REFERENCES users (id),

    CONSTRAINT uq_booking_slot_user UNIQUE (slot_id, user_id)
);

CREATE INDEX idx_bookings_user_id ON bookings (user_id);
CREATE INDEX idx_bookings_slot_user ON bookings (slot_id, user_id);

INSERT INTO users (name, email) VALUES
                                    ('gee',   'gee@gmail.com'),
                                    ('bee',     'bee@gmail.com'),
                                    ('cee', 'cee@gmail.com');

INSERT INTO concerts (name, venue, description) VALUES
                                                    ('Coldplay',   'GBK Stadium, Jakarta',  'Coldplay World Tour'),
                                                    ('Dewa 19',       'ICE BSD, Tangerang',    'Dewa 19 Reunion')

INSERT INTO ticket_slots (concert_id, sale_start, sale_end, total_tickets, remaining_tickets) VALUES
(1, NOW() + INTERVAL '1 day' + TIME '10:00:00', NOW() + INTERVAL '1 day' + TIME '10:20:00', 10000, 10000),
(2, NOW() + INTERVAL '2 day' + TIME '09:00:00', NOW() + INTERVAL '2 day' + TIME '09:30:00', 5000,  5000);