CREATE OR REPLACE FUNCTION check_action_possibility()
RETURNS TRIGGER AS $$
DECLARE
    person_location INT;
BEGIN
    -- Получаем id корабля, на котором сейчас находится человек
    SELECT current_ship INTO person_location 
    FROM people WHERE id = NEW.subject;

    -- Проверяем совпадает ли корабль человека с объектом действия
    IF person_location IS NULL OR person_location != NEW.object THEN
        RAISE EXCEPTION 'Человек (ID %) не может выполнить действие над кораблем (ID %), так как не находится на нем!', 
        NEW.subject, NEW.object;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_before_action
BEFORE INSERT OR UPDATE ON actions
FOR EACH ROW EXECUTE FUNCTION check_action_possibility();