PROGRAM TEST;
	VAR VAR1 : INTEGER;
	VAR VAR2 : FLOAT;
	VAR VAR3 : [10 .. 100];

	BEGIN
		VAR1 := 10;
		VAR2 := VAR3[0];
		LOOP
			VAR1 := 0;
		ENDLOOP;
	END.
	
