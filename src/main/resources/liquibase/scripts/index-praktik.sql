--liquibase formatted sql
--changeset your_name:003-add-search-indexes
CREATE INDEX IF NOT EXISTS idx_student_name ON студенты(name);
CREATE INDEX IF NOT EXISTS idx_faculty_name_color ON факультеты(name, color);

--rollback DROP INDEX IF EXISTS idx_student_name;
--rollback DROP INDEX IF EXISTS idx_faculty_name_color;