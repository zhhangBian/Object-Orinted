public class Soldier {
    private String name;
    private String incantation;

    public Soldier(String name, String incantation) {
        this.name = name;
        this.incantation = incantation;
    }

    public String GetName() {
        return this.name;
    }

    // this method means "cut" the "incantation"
    public void cutIncantation(int a, int b) {
        if (this.incantation != null) {
            if (a > b || a >= this.incantation.length()) {
                this.incantation = "";
                //this.incantation = null;
            } else {
                this.incantation = this.incantation.substring(
                        a, Integer.min(b + 1, incantation.length()));
            }
        }
    }

    //  2 means "to" , you will see it in the os codes  next semester
    public void appendStr2Incantation(String str) {
        if (this.incantation == null) {
            this.incantation = str;
        } else {
            this.incantation = this.incantation + str;
        }
    }

    public Soldier cloneSoldier() {
        return new Soldier(this.name, this.incantation);
    }

    // !!!!! Be careful, this method need to be carefully read and analyzed to identify bugs!
    public boolean notQualifiedByStandard(int standard) {
        int head = -1;
        int tail = 0;
        int totalCount = 0;

        if (this.incantation != null) {
            for (int i = 0; i < this.incantation.length(); i++) {
                if (this.incantation.charAt(i) == '@') {
                    head = i;
                    tail = head + 1;
                    break;
                }
            }

            while (tail < this.incantation.length()) {
                if (head == -1) {
                    break;
                }

                if (this.incantation.charAt(tail) != '@') {
                    tail++;
                    continue;
                }

                int count = 0;
                for (int i = head; i <= tail; i++) {
                    if (incantation.charAt(i) >= 'A' && incantation.charAt(i) <= 'Z') {
                        count++;
                    }
                    if (incantation.charAt(i) >= 'a' && incantation.charAt(i) <= 'z') {
                        count--;
                    }
                }
                if (count >= 0) {
                    totalCount++;
                }

                head = tail;
                tail++;
            }
        }

        return totalCount < standard;
    }

    public boolean hasString(String str) {
        if (this.incantation == null) {
            return false;
        } else {
            return incantation.contains(str);
        }
    }

    public boolean equal(Soldier soldier) {
        if (this.incantation == null && soldier.incantation == null) {
            return this.name.equals(soldier.name);
        } else if (this.incantation == null && soldier.incantation.equals("")) {
            return this.name.equals(soldier.name);
        } else if (this.incantation == null) {
            return false;
        } else if (this.incantation.equals("") && soldier.incantation == null) {
            return this.name.equals(soldier.name);
        } else if (soldier.incantation == null) {
            return false;
        } else {
            return this.name.equals(soldier.name)
                    && this.incantation.equals(soldier.incantation);
        }
    }
}