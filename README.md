gatling-maven-plugin-demo
=========================

Simple showcase of a maven project using the gatling-maven-plugin.

To test it out, simply execute the following command :

```
mvn gatling:execute -Dgatling.simulationClass=twitter.TwitterSimulation -Dusers=<no-of-concurent-users> -Dtweets=<no-of-your-tweets> -Dusername=<twitter-user-name> -Dpassword=<twitter-password>
```

### For below mvn command:

```
mvn gatling:execute -Dgatling.simulationClass=twitter.TwitterSimulation -Dusers=1 -Dtweets=298 -Dusername=**** -Dpassword=****
```

It will generate the following report on the terminal. But you can open the html report as well that has got nice charts to look at.

```
================================================================================
2013-08-23 15:49:55                                           1s elapsed
---- Twitter test --------------------------------------------------------------
[##########################################################################]100%
          waiting: 0      / running: 0      / done:1
---- Requests ------------------------------------------------------------------
> Global                                                   (OK=3      KO=0     )
> get login                                                (OK=1      KO=0     )
> post login                                               (OK=1      KO=0     )
> post login Redirect 1                                    (OK=1      KO=0     )
================================================================================

Simulation finished.
Generating reports...
Parsing log file(s)...
Parsing log file(s) done

================================================================================
---- Global Information --------------------------------------------------------
> numberOfRequests                                       3 (OK=3      KO=0     )
> minResponseTime                                      240 (OK=240    KO=-     )
> maxResponseTime                                      810 (OK=810    KO=-     )
> meanResponseTime                                     470 (OK=470    KO=-     )
> stdDeviation                                         245 (OK=245    KO=-     )
> percentiles1                                         810 (OK=810    KO=-     )
> percentiles2                                         810 (OK=810    KO=-     )
> meanNumberOfRequestsPerSecond                          2 (OK=2      KO=-     )
---- Response Time Distribution ------------------------------------------------
> t < 800 ms                                             2 ( 66%)
> 800 ms < t < 1200 ms                                   1 ( 33%)
> t > 1200 ms                                            0 (  0%)
> failed                                                 0 (  0%)
================================================================================
```
