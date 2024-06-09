package se2.group3.gameoflife.frontend.dto.cards;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CareerCardDTO extends CardDTO {

        private String id;
        private String name;
        private int salary;
        private int bonus;
        private boolean needsDiploma;

        public CareerCardDTO(@JsonProperty("id") String id,
                             @JsonProperty("name") String name,
                             @JsonProperty("salary") int salary,
                             @JsonProperty("bonus") int bonus,
                             @JsonProperty("needsDiploma") boolean needsDiploma) {
            this.id = id;
            this.name = name;
            this.salary = salary;
            this.bonus = bonus;
            this.needsDiploma = needsDiploma;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public int getBonus() {
            return bonus;
        }

        public void setBonus(int bonus) {
            this.bonus = bonus;
        }

        public boolean needsDiploma() {
            return needsDiploma;
        }

        public void setNeedsDiploma(boolean needsDiploma) {
            this.needsDiploma = needsDiploma;
        }
}
