SELECT
    s.name AS имя_студента,
    s.age AS возраст,
    f.name AS название_факультета
FROM студенты s
JOIN факультеты f ON s.faculty_id = f.faculty_id;

SELECT
    s.name AS имя_студента,
    s.age AS возраст,
    f.name AS название_факультета
FROM студенты s
JOIN факультеты f ON s.faculty_id = f.faculty_id
JOIN аватарки a ON s.id = a.student_id;