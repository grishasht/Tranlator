PROGRAM MAIN;

VAR
    (*
    TEST: [2..4[1..4 [3..5]]];
    *)
    TEST1:      INTEGER;
    VAR1:       INTEGER;
    VAR2:       INTEGER;
    VAR3:       INTEGER;
    VAR4:       INTEGER;
    VAR5:       INTEGER;
    VAR6:       INTEGER;
    RES1:       INTEGER;
    RES2:       INTEGER;
    RES3:       INTEGER;

BEGIN
(*Identifier appropriation*)
    RES1 := 10 / VAR3 + 5 * VAR2;
    (*****This must be correct*)
     RES2 := 15 / VAR4 - 30 / (VAR5 * 34 - 23 / VAR6);

END.