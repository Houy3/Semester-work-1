select to_timestamp('06.11.2022 12:30:41', 'dd.mm.yyyy hh24:mi:ss');

select ('06.11.2022'::timestamp - '05.11.2022'::timestamp) < interval '1 day';


select e.*, p.*
from events e
   , notes n
   , periods p
where e.note_id = n.id
  and n.timetable_id = 1
  and p.start_time < (date_trunc('day', to_timestamp('06.11.2022', 'dd.mm.yyyy hh24:mi:ss')::timestamp) + interval '1 day')
  and (date_trunc('day', to_timestamp('06.11.2022', 'dd.mm.yyyy hh24:mi:ss')::timestamp) <= p.end_time)
order by p.start_time;


select * from users where (email = 'ockap20030408@gmail.com' or nickname = 'Houy33') and id <> 4



