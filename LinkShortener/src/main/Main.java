package main;

import main.utils.LinkShortener;

public class Main {
    public static void main(String[] args) {
        LinkShortener linkShortener = new LinkShortener();

        linkShortener.getShortLink(
                "user",
                "https://apps.skillfactory.ru/learning/course/course-v1:SkillFactory+MIFIDEV+SEP2024/block-v1:SkillFactory+MIFIDEV+SEP2024+type@sequential+block@8784589536374f2b9617343653ac2312/block-v1:SkillFactory+MIFIDEV+SEP2024+type@vertical+block@6c460bd98e004ea2990b32036df94f50",
                0,
                10
                );
    }
}