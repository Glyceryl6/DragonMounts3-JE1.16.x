package net.dragonmounts3.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.scoreboard.Score;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.world.World;

import java.util.Map;

public class EntityUtil {
    public static CompoundNBT saveScoreboard(Entity entity, CompoundNBT compound) {
        Scoreboard scoreboard = entity.level.getScoreboard();
        String scoreboardName = entity.getScoreboardName();
        ScorePlayerTeam team = scoreboard.getPlayersTeam(scoreboardName);
        Map<ScoreObjective, Score> scores = scoreboard.getPlayerScores(scoreboardName);
        if (!scores.isEmpty()) {
            CompoundNBT scoresTag = new CompoundNBT();
            CompoundNBT lockedScoresTag = new CompoundNBT();
            Score cache;
            for (Map.Entry<ScoreObjective, Score> entry : scores.entrySet()) {
                cache = entry.getValue();
                if (cache.isLocked()) {
                    lockedScoresTag.putInt(entry.getKey().getName(), cache.getScore());
                } else {
                    scoresTag.putInt(entry.getKey().getName(), cache.getScore());
                }
            }
            if (!scoresTag.isEmpty()) {
                compound.put("Scores", scoresTag);
            }
            if (!lockedScoresTag.isEmpty()) {
                compound.put("LockedScores", lockedScoresTag);
            }
            if (team != null) {
                compound.putString("Team", team.getName());
            }
        }
        return compound;
    }

    public static <T extends Entity> T loadScores(T entity, CompoundNBT compound) {
        World level = entity.level;
        Scoreboard scoreboard = level.getScoreboard();
        String scoreboardName = entity.getScoreboardName();
        Map<ScoreObjective, Score> existingScores = level.getScoreboard().getPlayerScores(scoreboardName);
        CompoundNBT scores;
        ScoreObjective objective;
        Score score;
        if (compound.contains("Scores")) {
            scores = compound.getCompound("Scores");
            for (String name : scores.getAllKeys()) {
                objective = scoreboard.getOrCreateObjective(name);
                if (!existingScores.containsKey(objective)) {
                    score = scoreboard.getOrCreatePlayerScore(scoreboardName, objective);
                    score.setScore(scores.getInt(name));
                    score.setLocked(false);
                }
            }
        }
        if (compound.contains("LockedScores")) {
            scores = compound.getCompound("LockedScores");
            for (String name : scores.getAllKeys()) {
                objective = scoreboard.getOrCreateObjective(name);
                if (!existingScores.containsKey(objective)) {
                    score = scoreboard.getOrCreatePlayerScore(scoreboardName, objective);
                    score.setScore(scores.getInt(name));
                }
            }
        }
        return entity;
    }

    public static <T extends Entity> T loadScoreboard(T entity, CompoundNBT compound) {
        World level = entity.level;
        Scoreboard scoreboard = level.getScoreboard();
        String scoreboardName = entity.getScoreboardName();
        Map<ScoreObjective, Score> existingScores = level.getScoreboard().getPlayerScores(scoreboardName);
        CompoundNBT scores;
        ScoreObjective objective;
        Score score;
        // net.minecraft.entity.LivingEntity.readAdditionalSaveData
        if (compound.contains("Team", 8)) {
            scoreboard.addPlayerToTeam(scoreboardName, level.getScoreboard().getPlayerTeam(compound.getString("Team")));
        }
        if (compound.contains("Scores")) {
            scores = compound.getCompound("Scores");
            for (String name : scores.getAllKeys()) {
                objective = scoreboard.getOrCreateObjective(name);
                if (!existingScores.containsKey(objective)) {
                    score = scoreboard.getOrCreatePlayerScore(scoreboardName, objective);
                    score.setScore(scores.getInt(name));
                    score.setLocked(false);
                }
            }
        }
        if (compound.contains("LockedScores")) {
            scores = compound.getCompound("LockedScores");
            for (String name : scores.getAllKeys()) {
                objective = scoreboard.getOrCreateObjective(name);
                if (!existingScores.containsKey(objective)) {
                    score = scoreboard.getOrCreatePlayerScore(scoreboardName, objective);
                    score.setScore(scores.getInt(name));
                }
            }
        }
        return entity;
    }
}
