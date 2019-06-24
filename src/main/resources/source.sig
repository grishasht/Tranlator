PROGRAM MAIN;

VAR
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

    (* Must * work*)
    RES1 := 10 / VAR1 * 5 / VAR2 + 5 / VAR1;

    (*Identifier appropriation*)
    RES1 := 10 / VAR3 + 5 * VAR2;

    (*****This must be correct*)
    RES2 := 15 / VAR4 - 30 / (VAR5 * 34 - 23 / VAR6);

    RES3 := 25 * (VAR2 * 20 + VAR1 * (VAR5 / 5 - VAR5 * 5)) + 15 * 10;

END.