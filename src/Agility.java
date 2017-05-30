import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.interactive.GameObjects;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.utilities.Timer;
import org.dreambot.api.wrappers.interactive.GameObject;


import java.awt.*;

@ScriptManifest(name = "GnomeAgility70", author = "Laylaylom", description = "Gnome course", version = 1.0, category = Category.AGILITY)
public class Agility extends AbstractScript {

    public Area arealog = new Area(2473, 3437, 2475, 3436, 0);
    public Area net1Area = new Area(2471, 3429, 2476, 3426, 0);
    public Area treebranchup = new Area(2471, 3424, 2476, 3422, 1);
    public Area smallropearea = new Area(2476, 3420, 2477, 3419, 2);
    public Area ropearea = new Area(2472, 3421, 2477, 3419, 2);
    public Area treebranchdown = new Area(2483, 3420, 2488, 3422, 2);
    public Area gotonet2Area = new Area(2483, 3425, 2488, 3424, 0);
    public Area net2area = new Area(2483, 3426, 2488, 3416, 0);
    public Area pipearea = new Area(2482, 3436, 2489, 3426, 0);
    public int laps;
    private Timer t = new Timer();
    private int startedSkill;
    int anti;


    @Override
    public void onStart() {
        startedSkill = getSkills().getRealLevel(Skill.AGILITY);
        getSkillTracker().start(Skill.AGILITY);
    }

    public enum gnomeCourse{
        LOG, NET1, TREEBRANCHUP, ROPE, TREEBRANCHDOWN, NET2, PIPE, GOTOSTART, ANTI;
        }

    private gnomeCourse getState(){
        if(arealog.contains(getLocalPlayer())){
            return gnomeCourse.LOG;
        }
        if(net1Area.contains(getLocalPlayer())) {
            return gnomeCourse.NET1;
        }
        if(treebranchup.contains(getLocalPlayer())){
            return gnomeCourse.TREEBRANCHUP;
        }
        if(ropearea.contains(getLocalPlayer())){
            return gnomeCourse.ROPE;
        }
        if(treebranchdown.contains(getLocalPlayer())){
            return gnomeCourse.TREEBRANCHDOWN;
        }
        if(net2area.contains(getLocalPlayer())){
            return gnomeCourse.NET2;
        }
        if(pipearea.contains(getLocalPlayer())){
            return gnomeCourse.PIPE;
        }
        if (Calculations.random(45) == 1){
            return gnomeCourse.ANTI;
        }
        else{
            return gnomeCourse.LOG;
        }

    }


    @Override
    public int onLoop() {
        int select1 = Calculations.random(0,4);
        anti = Calculations.random(0, 10);

        switch (getState()) {
            case LOG:
                GameObject log = getGameObjects().closest("Log balance");
                if (log != null){
                    if (log.isOnScreen()){
                        if (log.interact("Walk-across")){
                            sleepUntil(() -> net1Area.contains(getLocalPlayer()), Calculations.random(9000, 9900));
                        }
                    }else{
                        getWalking().walk(arealog.getRandomTile());
                        sleep(Calculations.random(400, 900));
                        getCamera().rotateToEntity(log);
                        return Calculations.random(400, 650);
                    }
                }
                break;

            case NET1:
                GameObject net1 = getGameObjects().closest("Obstacle net");
                if (net1 != null){
                    sleep(Calculations.random(200,300));
                    if (net1.interact("Climb-over")){
                        sleep(Calculations.random(2600,3500));
                        sleepUntil(() -> treebranchup.contains(getLocalPlayer()), Calculations.random(1500, 2000));
                    }
                }
                break;

            case TREEBRANCHUP:
                GameObject treebranchupObject = getGameObjects().closest("Tree branch");
                if (treebranchup != null){
                    sleep(Calculations.random(200,300));
                    if (treebranchupObject.interact("Climb")){
                        sleep(Calculations.random(2000,2300));
                        sleepUntil(() -> ropearea.contains(getLocalPlayer()), Calculations.random(1000, 1200));
                    }
                }
                break;

            case ROPE:
                GameObject rope = getGameObjects().closest("Balancing rope");
                if (rope != null){
                    sleep(Calculations.random(200,300));
                        if (rope.interact("Walk-on")){
                            sleep(Calculations.random(800,1000));
                            sleepUntil(() -> treebranchdown.contains(getLocalPlayer()), Calculations.random(4650, 5200));
                    }
                }
                break;

            case TREEBRANCHDOWN:
                GameObject treebranchdownObject = getGameObjects().closest("Tree branch");
                if (treebranchdown != null){
                    if (treebranchdownObject.interact("Climb-down")){
                        sleep(Calculations.random(200,300));
                        sleepUntil(() -> getLocalPlayer().isStandingStill(), Calculations.random(2300, 3500));
                    }
                }
                break;

            case NET2:
                GameObject net2 = getGameObjects().closest("Obstacle net");
                if (net2 != null){
                    sleep(Calculations.random(200,300));
                        if (net2.interact("Climb-over")){
                            sleepUntil(() -> pipearea.contains(getLocalPlayer()), Calculations.random(4800, 5300));
                    }
                }
                break;

            case PIPE:
                GameObject pipe = getGameObjects().closest("Obstacle pipe");
                if (pipe != null){
                    sleep(Calculations.random(200,300));
                    if (pipe.interact("Squeeze-through")){
                        sleepUntil(() -> !pipearea.contains(getLocalPlayer()) && !getLocalPlayer().isAnimating() && getLocalPlayer().isStandingStill(), Calculations.random(8500, 8999));
                        sleep(Calculations.random(750,1000));
                        laps++;
                    }
                }
                break;

            case ANTI:
            switch (select1){
                case 1:
                    getTabs().open(Tab.STATS);
                    sleep(Calculations.random(400,600));
                    if (getSkills().hoverSkill(Skill.AGILITY)){
                        sleep(Calculations.random(3400, 4900));
                    }
                    getTabs().open(Tab.INVENTORY);
                    break;
                case 2:
                    getMouse().moveMouseOutsideScreen();
                    sleep(Calculations.random(4400,6000));
                    break;
                case 3:
                    sleep(Calculations.random(3300,5670));
                    break;
            }

                break;

            case GOTOSTART:
                sleep(Calculations.random(200,300));
                getWalking().walk(arealog.getRandomTile());
                sleepUntil(() -> arealog.contains(getLocalPlayer()), Calculations.random(5600, 6300));
                break;

        }

        return Calculations.random(400, 1200);
    }

    @Override
    public void onExit() {

    }

    @Override
    public void onPaint(Graphics graphics) {
        graphics.setColor(Color.ORANGE);
        graphics.drawString("State: " + getState().toString(), 5, 104);
        graphics.drawString("Experience(p/h): " + getSkillTracker().getGainedExperience(Skill.AGILITY) + "( " + getSkillTracker().getGainedExperiencePerHour(Skill.AGILITY) + " )" , 5, 120);
        graphics.drawString("Level (gained): " + getSkills().getRealLevel(Skill.AGILITY) + "( " + (getSkills().getRealLevel(Skill.AGILITY) - startedSkill) + " )", 5,136);
        graphics.drawString("Time Running: " + t.formatTime(), 5, 152);
        graphics.drawString("Total laps: " + laps, 5, 168);
    }
}