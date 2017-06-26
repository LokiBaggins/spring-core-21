DROP SCHEMA IF EXISTS springCore21;

CREATE TABLE eventCounters (
  id INTEGER PRIMARY KEY,
  event_id_ref INTEGER,
  name VARCHAR(30),
  times_called_by_name  INTEGER,
  times_prices_queried  INTEGER,
  tickets_booked  INTEGER
);