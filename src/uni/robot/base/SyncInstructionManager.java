package uni.robot.base;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Objeto que es capaz de sincronizar el paso de mensajes de hilos, uno o mas productores y un consumidor.
 * 
 * Cuando un hilo productor manda un mensaje a traves de este objeto, el mensaje es agregado a una fila y el hilo
 * es pausado, hasta que el hilo consumidor procese y mande una respuesta.
 * 
 * @author Fabio Kita
 *
 */
public class SyncInstructionManager {
	private Queue<SyncInstruction> instructionQueue;
	
	public SyncInstructionManager() {
		this.instructionQueue = new ConcurrentLinkedQueue<>();
	}
	
	//Producer Methods
	/**
	 * Metodo escluivo para el hilo productor. Pone un mensaje en la fila y bloquea hasta que un hilo consumidor
	 * mande una respuesta.<p>
	 * El mensaje no puede ser null.
	 * 
	 * @param message El mensaje a mandar
	 * @return La respuesta del hilo consumidor
	 */
	public Object sendInstruction(Object message) {
		SyncInstruction instruction = new SyncInstruction(message);
		instructionQueue.add(instruction);
		return instruction.waitInstruction();
	}
	
	//Consumer Methods
	/**
	 * Metodo exclusivo para el hilo consumidor. Lee el primer mensaje de la fila,
	 * 
	 * @return El primer mensaje de la fila,
	 */
	public Object readInstruction() {
		SyncInstruction instruction = instructionQueue.peek();
		if(instruction == null) return null;
		return instruction.getInstruction();
	}
	
	/**
	 * Marca como teminado y remueve el primer mensaje de la fila, desbloqueando el hilo consumidor y 
	 * mandando una respuesta.
	 * 
	 * @param message la respuesta a mandar
	 */
	public void finishInstruction(Object message) {
		SyncInstruction instruction = instructionQueue.poll();
		if(instruction == null) throw new RuntimeException("There's no current instruction.");
		instruction.markAsDone(message);
	}
	
	/**
	 * Marca como teminado y remueve el primer mensaje de la fila, desbloqueando el hilo consumidor y 
	 * mandando una respuesta.
	 */
	public void finishInstruction() {
		this.finishInstruction(null);
	}
}

class SyncInstruction{
	private boolean done;
	private Object message;
	private Object response;
	
	public SyncInstruction(Object message) {
		if(message == null) 
			throw new IllegalArgumentException("The message cannot be null.");
		
		this.done = false;
		this.message = message;
		this.response = null;
	}
	
	public Object getInstruction() {
		return message;
	}
	
	public synchronized Object waitInstruction(){
		try {
			while(!done) wait();
		}catch (InterruptedException e) {}
		
		return response;
	}
	
	public synchronized void markAsDone(Object response) {
		this.response = response;
		this.done = true;
		notify();
	}
}