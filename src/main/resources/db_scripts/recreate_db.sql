DROP SCHEMA spring_core_21 RESTRICT;
CREATE SCHEMA spring_core_21;

CREATE TABLE event_counters (
  id INTEGER PRIMARY KEY,
  event_id_ref INTEGER,
  event_name VARCHAR(30),
  times_called_by_name  INTEGER,
  times_prices_queried  INTEGER,
  tickets_booked  INTEGER
);