import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 * User: barclayadunn
 * Date: 7/21/13
 * Time: 5:41 PM
 */
public class PathMatcher {

    public static void main(String [] args) {
        int patternCount = 0, pathCount = 0;
        String input;
        List<String []> patterns = new LinkedList<String[]>();
        List<String []> paths = new LinkedList<String[]>();
        List<PathPatternMatch> pathPatternMatches = new LinkedList<PathPatternMatch>();
        Scanner sc;

        if (args.length > 0) {
            sc = new Scanner(args[0]);
        } else {
            sc = new Scanner(System.in);
        }

        while (sc.hasNext() && !"".equals(input = sc.nextLine())) {
            if (patternCount == 0) {
                // must be an int of patterns count, or an error.
                try {
                    patternCount = Integer.parseInt(input);
                } catch (NumberFormatException nfe) {
                    System.err.println("Error: First line must contain a number.");
                    return;
                }
            } else if (pathCount == 0) {
                // could be either patterns to parse, or the paths count
                try {
                    pathCount = Integer.parseInt(input);
                } catch (NumberFormatException nfe) {
                    // patterns to parse
                    // trim separators from start & end
                    input = trimEnd(trimFront(input, ","), ",");
                    String [] splitPatternInput = input.split("\\,");
                    patterns.add(splitPatternInput);
                }
            } else {
                // paths to parse
                // trim separators from start & end
                input = trimEnd(trimFront(input, "/"), "/");
                String [] splitPathInput = input.split("\\/");
                paths.add(splitPathInput);
            }
        }

        for (String [] pathIter : paths) {
            PathPatternMatch pathPatternMatch = new PathPatternMatch(pathIter);

            for (String [] patternIter : patterns) {
                int score = 0;
                if (pathIter.length != patternIter.length) {
                    score = -1;
                } else {
                    for (int i = 0; i < pathIter.length; i++) {
                        if (score == -1) {
                            continue;
                        } else if (pathIter[i].equals(patternIter[i])) {
                            // a direct match
                            score += 10;
                        } else if ("*".equals(patternIter[i])) {
                            // a wildcard
                            score += (i+1);
                        } else {
                            // encounter a non-match
                            score = -1;
                        }
                    }
                }
                if (score > pathPatternMatch.getScore()) {
                    pathPatternMatch.setPattern(patternIter);
                    pathPatternMatch.setScore(score);
                }
            }
            pathPatternMatches.add(pathPatternMatch);
        }

        for (PathPatternMatch ppm : pathPatternMatches) {
            if (ppm.getScore() == -1) {
                System.out.println("NO MATCH");
            } else {
//                System.out.println(join(ppm.getPattern(), ",") + "; score: " + ppm.getScore());
                System.out.println(join(ppm.getPattern(), ","));
            }
        }
    }

    private static String join(String [] arrayToJoin, String joiner) {
        String returnString = "";
        for (String item : arrayToJoin) {
            returnString += item + joiner;
        }
        int shortByOne = returnString.length() - 1;
        return returnString.substring(0,shortByOne);
    }

    private static String trimFront(String stringToTrim, String joiner) {
        String returnString = stringToTrim;
        if (stringToTrim.indexOf(joiner) == 0) {
            int shortByOne = stringToTrim.length() - 1;
            returnString = stringToTrim.substring(1, shortByOne);
        }
        return returnString;
    }

    private static String trimEnd(String stringToTrim, String joiner) {
        String returnString = stringToTrim;
        int shortByOne = stringToTrim.length() - 1;
        if (stringToTrim.lastIndexOf(joiner) == shortByOne) {
            returnString = stringToTrim.substring(0, shortByOne);
        }
        return returnString;
    }
}

class PathPatternMatch {
    private String [] path;
    private String [] pattern;
    private int score = -1;

    PathPatternMatch(String[] path) {
        this.path = path;
    }

    public String[] getPath() { return path; }
    public void setPath(String[] path) { this.path = path; }
    public String[] getPattern() { return pattern; }
    public void setPattern(String[] pattern) { this.pattern = pattern; }
    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }
}

/*
score +10 for each exact match plus pts for wildcard for whatever place (1,2,3,4) the wildcard is in
if the path & pattern lengths differ, instant non match (score -1)

path            pattern     score
/w/x/y/z        *,b,*       -1 (no match)
                a,*,*       -1
                *,*,c       -1
                foo,bar,baz -1
                w,x,*,*      27
                *,x,y,z      31
a/b/c           *,b,*        14 (1+3)
                a,*,*        15 (2+3)
                *,*,c        13 (1+2)
                foo,bar,baz -1
                w,x,*,*     -1
                *,x,y,z     -1
foo/            *,b,*       -1
                a,*,*       -1
                *,*,c       -1
                foo,bar,baz -1
                w,x,*,*     -1
                *,x,y,z     -1
foo/bar/        *,b,*       -1
                a,*,*       -1
                *,*,c       -1
                foo,bar,baz -1
                w,x,*,*     -1
                *,x,y,z     -1
foo/bar/baz/    *,b,*       -1
                a,*,*       -1
                *,*,c       -1
                foo,bar,baz  30
                w,x,*,*     -1
                *,x,y,z     -1
*/
