#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#define MAX 1031

int main() {
    int wins = 0;
    int miss = 0;
    int r[MAX];
    int i, guess;

    srandom(time(0));

    for (i = 0; i < 31; i++) {
        r[i] = random();
    }

    for (; i < MAX; i++) {
        r[i] = random();
        guess = r[i-31] + r[i-3];

        if (guess < 0) {
            guess += 2147483647;
            guess += 1;
        }

        if (r[i] == guess) {
          wins++;
        } else if (r[i] == guess+1) {
          miss++;
        }

        // printf("%d %d\t%d\tdiff: %d\n", i, r[i], guess, r[i] - guess);
    }
    int times = MAX - 31;
    printf("wins: %d out of %d\n", wins, times);
    printf("miss: %d out of %d\n", miss, times);
}
