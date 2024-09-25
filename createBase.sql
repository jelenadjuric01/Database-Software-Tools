
DROP TABLE [ZahtevZaKurira]
go

DROP TABLE [Vozi]
go

DROP TABLE [Ponuda]
go

DROP TABLE [Paket]
go

DROP TABLE [Opstina]
go

DROP TABLE [Grad]
go

DROP TABLE [Kurir]
go

DROP TABLE [Vozilo]
go

DROP TABLE [Administrator]
go

DROP TABLE [Korisnik]
go

CREATE TABLE [Administrator]
( 
	[KorIme]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Grad]
( 
	[IdG]                bigint  IDENTITY ( 1,1 )  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[PostanskiBroj]      varchar(100)  NOT NULL 
)
go

CREATE TABLE [Korisnik]
( 
	[Ime]                varchar(100)  NOT NULL ,
	[Prezime]            varchar(100)  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL ,
	[Sifra]              varchar(100)  NOT NULL ,
	[BrPoslatihPaketa]   integer  NOT NULL 
	CONSTRAINT [DefaultBr_1546461045]
		 DEFAULT  0
	CONSTRAINT [VeceOd0_521186331]
		CHECK  ( BrPoslatihPaketa >= 0 )
)
go

CREATE TABLE [Kurir]
( 
	[BrisporucenihPak]   integer  NULL 
	CONSTRAINT [DefaultBr_605020385]
		 DEFAULT  0
	CONSTRAINT [VeceOd0_526789720]
		CHECK  ( BrisporucenihPak >= 0 ),
	[Profit]             decimal(10,3)  NULL 
	CONSTRAINT [DefaultBr_167117564]
		 DEFAULT  0,
	[Status]             integer  NOT NULL 
	CONSTRAINT [DefaultBr_68746743]
		 DEFAULT  0
	CONSTRAINT [Status_1613851623]
		CHECK  ( [Status]=0 OR [Status]=1 ),
	[RegBr]              varchar(100)  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Opstina]
( 
	[IdO]                bigint  IDENTITY ( 1,1 )  NOT NULL ,
	[Naziv]              varchar(100)  NOT NULL ,
	[X]                  integer  NOT NULL ,
	[Y]                  integer  NOT NULL ,
	[IdG]                bigint  NOT NULL 
)
go

CREATE TABLE [Paket]
( 
	[IdP]                bigint  IDENTITY ( 1,1 )  NOT NULL ,
	[Status]             integer  NOT NULL 
	CONSTRAINT [DefaultBr_184874739]
		 DEFAULT  0
	CONSTRAINT [StatusPaket_687572560]
		CHECK  ( [Status]=0 OR [Status]=1 OR [Status]=2 OR [Status]=3 ),
	[Cena]               decimal(10,3)  NOT NULL ,
	[VremePrihZaht]      datetime  NULL ,
	[Kurir]              varchar(100)  NULL ,
	[Korisnik]           varchar(100)  NOT NULL ,
	[TipPaketa]          integer  NOT NULL 
	CONSTRAINT [Gorivo_69075431]
		CHECK  ( [TipPaketa]=0 OR [TipPaketa]=1 OR [TipPaketa]=2 ),
	[Tezina]             decimal(10,3)  NOT NULL 
	CONSTRAINT [VeceOd0_1752827977]
		CHECK  ( Tezina >= 0 ),
	[IdOPre]             bigint  NOT NULL ,
	[IdOIsp]             bigint  NOT NULL 
)
go

CREATE TABLE [Ponuda]
( 
	[Procenat]           integer  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL ,
	[IdPon]              bigint  IDENTITY ( 1,1 )  NOT NULL ,
	[IdP]                bigint  NOT NULL 
)
go

CREATE TABLE [Vozi]
( 
	[IdP]                bigint  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL 
)
go

CREATE TABLE [Vozilo]
( 
	[RegBr]              varchar(100)  NOT NULL ,
	[TipGoriva]          integer  NOT NULL 
	CONSTRAINT [Gorivo_252248955]
		CHECK  ( [TipGoriva]=0 OR [TipGoriva]=1 OR [TipGoriva]=2 ),
	[Potrosnja]          decimal(10,3)  NOT NULL 
)
go

CREATE TABLE [ZahtevZaKurira]
( 
	[RegBr]              varchar(100)  NOT NULL ,
	[KorIme]             varchar(100)  NOT NULL 
)
go

ALTER TABLE [Administrator]
	ADD CONSTRAINT [XPKAdministrator] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XPKGrad] PRIMARY KEY  CLUSTERED ([IdG] ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK1Grad] UNIQUE ([PostanskiBroj]  ASC)
go

ALTER TABLE [Grad]
	ADD CONSTRAINT [XAK2Grad] UNIQUE ([Naziv]  ASC)
go

ALTER TABLE [Korisnik]
	ADD CONSTRAINT [XPKKorisnik] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XPKKurir] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [XAK1Kurir] UNIQUE ([RegBr]  ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XPKOpstina] PRIMARY KEY  CLUSTERED ([IdO] ASC)
go

ALTER TABLE [Opstina]
	ADD CONSTRAINT [XAK1Opstina] UNIQUE ([IdG]  ASC,[Naziv]  ASC)
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [XPKPaket] PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [XPKPonuda] PRIMARY KEY  CLUSTERED ([IdPon] ASC)
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [XPKVozi] PRIMARY KEY  CLUSTERED ([IdP] ASC)
go

ALTER TABLE [Vozilo]
	ADD CONSTRAINT [XPKVozilo] PRIMARY KEY  CLUSTERED ([RegBr] ASC)
go

ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [XPKZahtevZaKurira] PRIMARY KEY  CLUSTERED ([KorIme] ASC)
go


ALTER TABLE [Administrator]
	ADD CONSTRAINT [R_2] FOREIGN KEY ([KorIme]) REFERENCES [Korisnik]([KorIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go


ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_3] FOREIGN KEY ([KorIme]) REFERENCES [Korisnik]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Kurir]
	ADD CONSTRAINT [R_4] FOREIGN KEY ([RegBr]) REFERENCES [Vozilo]([RegBr])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Opstina]
	ADD CONSTRAINT [R_1] FOREIGN KEY ([IdG]) REFERENCES [Grad]([IdG])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go


ALTER TABLE [Paket]
	ADD CONSTRAINT [R_6] FOREIGN KEY ([Korisnik]) REFERENCES [Korisnik]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_5] FOREIGN KEY ([Kurir]) REFERENCES [Kurir]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_17] FOREIGN KEY ([IdOPre]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Paket]
	ADD CONSTRAINT [R_18] FOREIGN KEY ([IdOIsp]) REFERENCES [Opstina]([IdO])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_9] FOREIGN KEY ([KorIme]) REFERENCES [Kurir]([KorIme])
		ON DELETE CASCADE
		ON UPDATE CASCADE
go

ALTER TABLE [Ponuda]
	ADD CONSTRAINT [R_19] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_13] FOREIGN KEY ([IdP]) REFERENCES [Paket]([IdP])
		ON DELETE NO ACTION
		ON UPDATE CASCADE
go

ALTER TABLE [Vozi]
	ADD CONSTRAINT [R_14] FOREIGN KEY ([KorIme]) REFERENCES [Kurir]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go


ALTER TABLE [ZahtevZaKurira]
	ADD CONSTRAINT [R_16] FOREIGN KEY ([KorIme]) REFERENCES [Korisnik]([KorIme])
		ON DELETE NO ACTION
		ON UPDATE NO ACTION
go
