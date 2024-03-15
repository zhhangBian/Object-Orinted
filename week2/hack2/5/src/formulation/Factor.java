package formulation;

import calc.Poly;

public interface Factor {
    void setIndex(int input);

    Factor replace(char source, Factor target);

    Factor clonec();

    Poly toPoly();
}
