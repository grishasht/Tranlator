PROGRAM MAIN;

VAR
    IDENT1:     INTEGER;
    IDENT2:     INTEGER;
    SUM1:       INTEGER;
    SUM2:       INTEGER;
    SUM3:       INTEGER;
    SUM4:       INTEGER;
    SUM5:       INTEGER;
    MULT1:      INTEGER;
    MULT2:      INTEGER;
    MULT3:      INTEGER;
    DIV1:       INTEGER;
    DIV2:       INTEGER;
    DIV3:       INTEGER;
    RES:        INTEGER;

BEGIN

    (*Identifier appropriation*)
    IDENT1 := 5;
    IDENT2 := 15;

    (*****This must be correct*)
    SUM1 := IDENT1 + IDENT2;
    SUM2 := IDENT1 - IDENT2;
    SUM3 := -(IDENT2 - IDENT1);
    SUM4 := -(IDENT1 + IDENT2);
    SUM5 := -(IDENT1 - (IDENT1 + (IDENT2-SUM1)));

    (* *)
    MULT1 := IDENT1 * IDENT2;
    MULT2 := MULT1 * IDENT1 * IDENT2;
    MULT3 := -(MULT1 * MULT2);


    DIV1 := IDENT2 / IDENT1;
    DIV2 := -(IDENT2 / IDENT1);
    DIV3 := DIV2 / DIV1;

    RES := -(((IDENT1 + IDENT2) * (IDENT2 - IDENT1)) / 5);

END.