var status = -1;

function action(mode, type, selection) {
    if (cm.isQuestActive(3114)) {
	cm.forceCompleteQuest(3114);
    }
    cm.playSound(false, "orbis/si");
    cm.dispose();
}