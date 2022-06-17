CREATE TABLE IF NOT EXISTS parking_complex (
   id uuid not null,
   parking varchar(255),
   entry_point varchar(255)[],
   created_at timestamp,
   updated_at timestamp,
   CONSTRAINT pk_parking_complex PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS slot (
   id uuid not null,
   code varchar(255),
   name varchar(255),
   price numeric,
   created_at timestamp,
   updated_at timestamp,
   CONSTRAINT pk_slot PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS complex_slot_mapping (
   id uuid not null,
   parking_complex_id uuid not null,
   slot_id uuid not null,
   code varchar(255),
   CONSTRAINT pk_complex_slot_mapping PRIMARY KEY (id),
   CONSTRAINT complex_slot_mapping_complex_id_fkey FOREIGN KEY (parking_complex_id) REFERENCES parking_complex(id),
   CONSTRAINT complex_slot_mapping_slot_id_fkey FOREIGN KEY (slot_id) REFERENCES slot(id)
);


CREATE TABLE IF NOT EXISTS parking (
   id uuid not null,
   complex_slot_mapping_id uuid not null,
   plate_number varchar(15),
   category varchar(15),
   park_in timestamp,
   park_out timestamp,
   status varchar(15),
   CONSTRAINT pk_parking PRIMARY KEY (id),
   CONSTRAINT parking_complex_slot_mapping_id_fkey FOREIGN KEY (complex_slot_mapping_id) REFERENCES complex_slot_mapping(id)
);
