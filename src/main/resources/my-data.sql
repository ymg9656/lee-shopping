-- 카테고리 테이블 생성
CREATE TABLE category (
    id VARCHAR(20) PRIMARY KEY,
    sort_no smallInt,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO category (id,sort_no) VALUES ('상의',1);
INSERT INTO category (id,sort_no) VALUES ('아우터',2);
INSERT INTO category (id,sort_no) VALUES ('바지',3);
INSERT INTO category (id,sort_no) VALUES ('스니커즈',4);
INSERT INTO category (id,sort_no) VALUES ('가방',5);
INSERT INTO category (id,sort_no) VALUES ('모자',6);
INSERT INTO category (id,sort_no) VALUES ('양말',7);
INSERT INTO category (id,sort_no) VALUES ('액세서리',8);

-- brand 테이블 생성
CREATE TABLE brand (
    id VARCHAR(10) PRIMARY KEY,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO brand (id) VALUES ('A');
INSERT INTO brand (id) VALUES ('B');
INSERT INTO brand (id) VALUES ('C');
INSERT INTO brand (id) VALUES ('D');
INSERT INTO brand (id) VALUES ('E');
INSERT INTO brand (id) VALUES ('F');
INSERT INTO brand (id) VALUES ('G');
INSERT INTO brand (id) VALUES ('H');
INSERT INTO brand (id) VALUES ('I');


-- product 테이블 생성
CREATE TABLE product (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    brand_id varchar(10) NOT NULL,
    category_id varchar(20) NOT NULL,
    price INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP

);
CREATE INDEX idx_brand_id ON product(brand_id);
CREATE INDEX idx_category_id ON product(category_id);

INSERT INTO product (brand_id,category_id,price) VALUES ('A','상의',11200);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','아우터',5500);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','바지',4200);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','스니커즈',9000);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','가방',2000);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','모자',1700);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','양말',1800);
INSERT INTO product (brand_id,category_id,price) VALUES ('A','액세서리',2300);

INSERT INTO product (brand_id,category_id,price) VALUES ('B','상의',10500);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','아우터',5900);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','바지',3800);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','스니커즈',9100);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','가방',2100);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','모자',2000);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','양말',2000);
INSERT INTO product (brand_id,category_id,price) VALUES ('B','액세서리',2200);

INSERT INTO product (brand_id,category_id,price) VALUES ('C','상의',10000);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','아우터',6200);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','바지',3300);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','스니커즈',9200);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','가방',2200);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','모자',1900);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','양말',2200);
INSERT INTO product (brand_id,category_id,price) VALUES ('C','액세서리',2100);


INSERT INTO product (brand_id,category_id,price) VALUES ('D','상의',10100);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','아우터',5100);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','바지',3000);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','스니커즈',9500);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','가방',2500);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','모자',1500);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','양말',2400);
INSERT INTO product (brand_id,category_id,price) VALUES ('D','액세서리',2000);

INSERT INTO product (brand_id,category_id,price) VALUES ('E','상의',10700);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','아우터',5000);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','바지',3800);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','스니커즈',9900);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','가방',2300);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','모자',1800);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','양말',2100);
INSERT INTO product (brand_id,category_id,price) VALUES ('E','액세서리',2100);

INSERT INTO product (brand_id,category_id,price) VALUES ('F','상의',11200);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','아우터',7200);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','바지',4000);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','스니커즈',9300);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','가방',2100);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','모자',1600);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','양말',2300);
INSERT INTO product (brand_id,category_id,price) VALUES ('F','액세서리',1900);

INSERT INTO product (brand_id,category_id,price) VALUES ('G','상의',10500);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','아우터',5800);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','바지',3900);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','스니커즈',9000);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','가방',2200);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','모자',1700);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','양말',2100);
INSERT INTO product (brand_id,category_id,price) VALUES ('G','액세서리',2000);

INSERT INTO product (brand_id,category_id,price) VALUES ('H','상의',10800);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','아우터',6300);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','바지',3100);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','스니커즈',9700);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','가방',2100);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','모자',1600);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','양말',2000);
INSERT INTO product (brand_id,category_id,price) VALUES ('H','액세서리',2400);

INSERT INTO product (brand_id,category_id,price) VALUES ('I','상의',11400);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','아우터',6700);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','바지',3200);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','스니커즈',9500);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','가방',2400);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','모자',1700);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','양말',1700);
INSERT INTO product (brand_id,category_id,price) VALUES ('I','액세서리',2400);


-- category_brand_rank 테이블 생성
CREATE TABLE product_rank (
    rank_key varchar(50) NOT NULL,
    brand_id	varchar(10)	NOT NULL,
    category_id	varchar(20)	NOT NULL,
    product_id	bigint	NOT NULL,
    price	int	NOT NULL,
    created_at	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at	TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (rank_key, brand_id, category_id, product_id)
);
