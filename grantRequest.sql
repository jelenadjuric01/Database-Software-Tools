USE [TransportPaketa]
GO
/****** Object:  StoredProcedure [dbo].[grantRequest]    Script Date: 13.6.2024. 18:55:06 ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[grantRequest]
	@Username varchar(100)
AS
BEGIN
	declare @RegNum varchar(100)
	
		set @RegNum = (select RegBr from ZahtevZaKurira where ZahtevZaKurira.KorIme=@Username)
		insert into Kurir(KorIme,BrisporucenihPak,Profit,Status,RegBr) values (@Username,0,0,0,@RegNum)
		delete from ZahtevZaKurira where KorIme=@Username
		delete from ZahtevZaKurira where RegBr=@RegNum

END
