// file = 0; split type = patterns; threshold = 100000; total count = 0.
#include <stdio.h>
#include <stdlib.h>
#include <strings.h>
#include "rmapats.h"

void  hsG_0__0 (struct dummyq_struct * I1364, EBLK  * I1359, U  I709);
void  hsG_0__0 (struct dummyq_struct * I1364, EBLK  * I1359, U  I709)
{
    U  I1625;
    U  I1626;
    U  I1627;
    struct futq * I1628;
    struct dummyq_struct * pQ = I1364;
    I1625 = ((U )vcs_clocks) + I709;
    I1627 = I1625 & ((1 << fHashTableSize) - 1);
    I1359->I754 = (EBLK  *)(-1);
    I1359->I755 = I1625;
    if (0 && rmaProfEvtProp) {
        vcs_simpSetEBlkEvtID(I1359);
    }
    if (I1625 < (U )vcs_clocks) {
        I1626 = ((U  *)&vcs_clocks)[1];
        sched_millenium(pQ, I1359, I1626 + 1, I1625);
    }
    else if ((peblkFutQ1Head != ((void *)0)) && (I709 == 1)) {
        I1359->I757 = (struct eblk *)peblkFutQ1Tail;
        peblkFutQ1Tail->I754 = I1359;
        peblkFutQ1Tail = I1359;
    }
    else if ((I1628 = pQ->I1267[I1627].I777)) {
        I1359->I757 = (struct eblk *)I1628->I775;
        I1628->I775->I754 = (RP )I1359;
        I1628->I775 = (RmaEblk  *)I1359;
    }
    else {
        sched_hsopt(pQ, I1359, I1625);
    }
}
#ifdef __cplusplus
extern "C" {
#endif
void SinitHsimPats(void);
#ifdef __cplusplus
}
#endif
