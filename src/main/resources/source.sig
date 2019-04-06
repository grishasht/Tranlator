PROGRAM MAIN;

   VAR IDENT1:     INTEGER;
   VAR IDENT2:     INTEGER;
   VAR SUM1:       INTEGER;
   VAR SUM2:       INTEGER;
   VAR SUM3:       INTEGER;
   VAR SUM4:       INTEGER;
   VAR SUM5:       INTEGER;
   VAR MULT1:      INTEGER;
   VAR MULT2:      INTEGER;
   VAR MULT3:      INTEGER;
   VAR DIV1:       INTEGER;
   VAR DIV2:       INTEGER;
   VAR DIV3:       INTEGER;
   VAR RES:        INTEGER;

BEGIN
    (*Initialization of identifiers*)
    IDENT1 := 5;
    IDENT2 := 15;

    (*Some examples with adding identifiers*)
    SUM1 := IDENT1 + IDENT2;
    SUM2 := IDENT1 - IDENT2;
    SUM3 := -(IDENT2 - IDENT1);
    SUM4 := -(IDENT1 + IDENT2);
    SUM5 := -(IDENT1 - (IDENT1 + (IDENT2-SUM1)));

    (*Some examples with multiplying identifiers*)
    MULT1 := IDENT1 * IDENT2;
    MULT2 := MULT1 * IDENT1 * IDENT2;
    MULT3 := -(MULT1 * MULT2);

    (*Some examples with dividing of identifiers*)
    DIV1 := IDENT2 / IDENT1;
    DIV2 := -(IDENT2 / IDENT1);
    DIV3 := DIV2 / DIV1;

    (*Some example with all operations with identifiers*)
    RES := -(((IDENT1 + IDENT2) * (IDENT2 - IDENT1)) / 5);

END.