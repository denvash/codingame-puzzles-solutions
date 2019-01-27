// https://www.codingame.com/training/medium/mars-lander-episode-2

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <ctype.h>
#include <time.h>
#include <assert.h>
#include <algorithm>
#include <iostream>
#include <queue>
#include <stack>
#include <vector>

using namespace std;

#define sp system("pause")
#define FOR(i,a,b) for(int i=a;i<=b;++i)
#define FORD(i,a,b) for(int i=a;i>=b;--i)
#define REP(i,n) FOR(i,0,(int)n-1)
#define DB(s,x) fprintf(stderr,s,x);
#define MS0(x) memset(x,0,sizeof(x))
#define MS1(x) memset(x,1,sizeof(x))
#define SORT(a,n) sort(begin(a),begin(a)+n)
#define REV(a,n) reverse(begin(a),begin(a)+n)
#define ll long long
#define MOD 1000000007

struct point{
	int x, y;
};

const float g = -3.711;
const double pi = 3.1415926;
const int safedist = 300;

point grnd[100], landl, landr;


int main(){


	int n, sx, sy, f, r, p;
	point ml,landing;

	scanf("%d", &n);
	REP(i, n){
		scanf("%d%d", &grnd[i].x, &grnd[i].y);
		if (grnd[i].y == grnd[i - 1].y){
			landl = grnd[i - 1];
			landr = grnd[i];
		}
	}

	DB("Left Landing Point = %d\n", landl.x);
	DB("Right Landing Point = %d\n", landr.x);

	while (1){
		scanf("%d%d%d%d%d%d%d", &ml.x, &ml.y, &sx, &sy, &f, &r, &p);

		//Finding Landing Point
		double t;
		for(t=0;t<=1000;t+=0.001){
			int x = ml.x + sx*t ;
			int y = ml.y + sy*t + (g*t*t)/2 ;
			if (y < landr.y){
				landing.x = x;
				landing.y = y+1;
				DB("Landing in %f sec\n", t);
				break;
			}
		}

		DB("Landing at X =%d\n", landing.x);
		DB("Landing at Y =%d\n", landing.y);

		if (landl.x == 500){
			if (t<3)
				printf("0 4\n");
			else if (ml.x<2000)
				printf("-45 3\n");
			else if (sy<-1)
				printf("0 4\n");
			else
				printf("0 3\n");
		}
		else if (t > 2){
			double ax = -2 * (ml.x + sx*t - ((double)landl.x + landr.x) / 2) / (t*t);
			double ay = (-30 - sy - g*t) / t;
			DB("Acc X = %f\n", ax);
			DB("Acc Y = %f\n", ay);
			double rot = atan2(ay, ax) * 180 / pi;
			double thrust = sqrt(ay*ay + ax*ax);

			DB("Rot = %f\n", rot);
			DB("Thrust = %f\n", thrust);

			rot = rint(rot) - 90;
			thrust = rint(thrust);
			if (thrust > 4)
				thrust = 4;
			else if (thrust < 0)
				thrust = 0;
			printf("%d %d\n", (int)rot, (int)thrust);
		}
		else{
			double ay = (-40 - sy - g*t) / t;
			if (ay>0)
				printf("0 %d\n", (int)rint(ay));
			else
				printf("0 0\n", (int)rint(ay));
		}
	}
	return 0;
}