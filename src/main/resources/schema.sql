DROP TABLE IF EXISTS
	purchase_item,
	purchase,
	provider,
    financial_event,
    component,
	product_component,
    request_product,
    request,
    product,
    customer;

CREATE TABLE component ( --cmp
    cmp_id varchar(10) DEFAULT substring(MD5(random()::text), 0, 11) PRIMARY KEY,
    cmp_created_at timestamp with time zone NOT NULL,
    cmp_deleted_at timestamp with time zone,
    cmp_name varchar NOT NULL,
    measure varchar NOT NULL,
    base_buy_height decimal NOT NULL,
    base_buy_width decimal NOT NULL,
    base_buy_amount decimal NOT NULL,
    base_buy_paid_value decimal NOT NULL,
    photo_address varchar,
	CONSTRAINT component_name_uk UNIQUE(cmp_name)
);

CREATE TABLE product ( --pdt
    pdt_id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    pdt_created_at timestamp with time zone NOT NULL,
    pdt_deleted_at timestamp with time zone,
    pdt_name varchar NOT NULL UNIQUE,
    status varchar NOT NULL,
    description varchar,
    price decimal NOT NULL,
    photo_address varchar,
    production_duration_in_minutes int NOT NULL
);

CREATE TABLE product_component ( --pco
    pco_pdt_id uuid NOT NULL REFERENCES product(pdt_id),
    pco_cmp_id varchar REFERENCES component(cmp_id),
	height decimal NOT NULL,
	width decimal NOT NULL,
    amount decimal NOT NULL,
    PRIMARY KEY(pco_pdt_id, pco_cmp_id)
);

CREATE TABLE customer ( --ctm
	ctm_id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    ctm_created_at timestamp with time zone NOT NULL,
    phone varchar NOT NULL,
	ctm_name varchar NOT NULL,
    facebook_chat_number varchar,
    rating int,
    CONSTRAINT customer_phone_uk UNIQUE(phone)
);

CREATE TABLE request ( --rqt
	rqt_id serial PRIMARY KEY,
    rqt_ctm_id uuid REFERENCES customer(ctm_id) NOT NULL,
    rqt_created_at timestamp with time zone NOT NULL,
    rqt_canceled_at timestamp with time zone,
    due_date date NOT NULL,
    status varchar NOT NULL,
	notes varchar,
	rating int
);

CREATE TABLE request_product ( --rpd
	rpd_rqt_id int REFERENCES request(rqt_id),
	rpd_pdt_id uuid REFERENCES product(pdt_id),
	rpd_created_at timestamp with time zone NOT NULL,
	product_document jsonb NOT NULL, --properties[component,measure,width,height,amount,paid_value]
	calculated_production_cost decimal NOT NULL,
	declared_production_cost decimal NOT NULL,
	unitary_value decimal NOT NULL,
	amount decimal NOT NULL,
	notes varchar,
	file_path varchar,
	file_link varchar,
	PRIMARY KEY(rpd_rqt_id, rpd_pdt_id)
);

CREATE TABLE financial_event ( --fev
	fev_id serial PRIMARY KEY,
    fev_created_at timestamp with time zone NOT NULL,
	fev_date date NOT NULL,
	fev_rqt_id int REFERENCES request(rqt_id),
	event varchar NOT NULL,
	description varchar,
	operation varchar NOT NULL,
	wallet varchar NOT NULL,
	fev_value decimal NOT NULL,
	percentage int
	--voucher_path varchar
);


--*******************************v2.0***************************************

--DROP TABLE IF EXISTS purchase_item, purchase, provider;

CREATE TABLE provider ( --pvd
    pvd_id uuid DEFAULT gen_random_uuid() PRIMARY KEY,
    pvd_name varchar NOT NULL UNIQUE,
    phone varchar
);

CREATE TABLE purchase ( --prc
    prc_id serial PRIMARY KEY,
    prc_pvd_id uuid REFERENCES provider(pvd_id) NOT NULL,
    prc_date date NOT NULL,
    prc_value decimal NOT NULL
);

CREATE TABLE purchase_item ( --pci
	pci_id serial PRIMARY KEY,
    pci_prc_id int REFERENCES purchase(prc_id) NOT NULL,
    pci_cmp_id varchar(10) REFERENCES component(cmp_id),
    pci_type varchar NOT NULL, --DELIVERY,COMPONENT,TAX,OTHER
    pci_name varchar NOT NULL,
    measure varchar NOT NULL, --UNIT,CM,CM2
	price decimal NOT NULL CHECK(amount > 0),
	amount decimal NOT NULL CHECK(amount > 0),
	width decimal NOT NULL,
	height decimal NOT NULL,
	prc_created_at timestamp with time zone NOT NULL,
    status varchar NOT NULL,
    CONSTRAINT purchase_component_uk UNIQUE(pci_prc_id, pci_cmp_id)
);

CREATE INDEX idx_financial_event_date ON financial_event(fev_date);
ALTER TABLE financial_event ALTER COLUMN description SET NOT NULL;
ALTER TABLE financial_event ALTER COLUMN fev_value TYPE decimal(10,2);
ALTER TABLE financial_event ADD COLUMN fev_prc_id int REFERENCES purchase(prc_id);
ALTER TABLE financial_event ADD COLUMN banking_operation bool NOT NULL;

ALTER TABLE component ADD COLUMN code VARCHAR;

