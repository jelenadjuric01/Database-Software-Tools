
CREATE TRIGGER TR_TransportOffer_AcceptRequest
   ON  Paket 
   AFTER update
AS 
BEGIN
	declare @kursor Cursor
	declare @IdP bigint
	set @kursor = cursor for
	select IdP
	from inserted
	where Status = 1

	open @kursor

	fetch next from @kursor
	into @IdP

	while @@FETCH_STATUS =0
	begin
	delete from Ponuda
	where IdP=@IdP

	fetch next from @kursor
	into @IdP
	end

	close @kursor
	deallocate @kursor

END
GO
