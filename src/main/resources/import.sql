insert into test.relationship (id,
                          accepted_date,
                          end_date,
                          created_date,
                          status_enum,
                          submitted_date,
                          archived_date,
                          archived_flag,
                          reminder_flag)
values
  (next value for hibernate_sequence,
   current time - 9 days,
   NULL,
   current time - 12 days,
   'ACTIVE',
   current time - 11 days,
   NULL,
   0,
   0),
  (next value for hibernate_sequence,
   NULL,
   NULL,
   current time - 11 days,
   'PENDING',
   current time - 10 days,
   NULL,
   0,
   0);