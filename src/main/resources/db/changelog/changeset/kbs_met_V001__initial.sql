CREATE TABLE IF NOT EXISTS equipment (
    id BIGSERIAL PRIMARY KEY,
    metrologist VARCHAR(255) NOT NULL,
    number_upsz VARCHAR(255) NOT NULL,
    type_num_integral VARCHAR(255) NOT NULL,
    manufacture_num_integral VARCHAR(255) NOT NULL,
    type_num_iva VARCHAR(255) NOT NULL,
    manufacture_num_iva VARCHAR(255) NOT NULL,
    type_num_tl VARCHAR(255) NOT NULL,
    manufacture_num_tl VARCHAR(255) NOT NULL,
    snils VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Таблица water_meter
CREATE TABLE IF NOT EXISTS water_meter (
    id BIGSERIAL PRIMARY KEY,
    mitype_number VARCHAR(255) NOT NULL,
    manufacture_num VARCHAR(255) NOT NULL,
    modification VARCHAR(255) NOT NULL,
    manufactured_year INT,
    vrf_date DATE NOT NULL,
    valid_date DATE NOT NULL,
    temperature VARCHAR(100) NOT NULL,
    pressure VARCHAR(100) NOT NULL,
    humidity VARCHAR(100) NOT NULL,
    other VARCHAR(500) NOT NULL,
    address VARCHAR(500) NOT NULL,
    act_number VARCHAR(255) NOT NULL,
    equipment_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_water_meter_equipment
        FOREIGN KEY (equipment_id)
        REFERENCES equipment(id)
);

-- Создание индексов
CREATE INDEX IF NOT EXISTS idx_water_meter_manufacture_num ON water_meter(manufacture_num);