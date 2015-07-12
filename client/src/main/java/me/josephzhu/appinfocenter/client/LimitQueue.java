package me.josephzhu.appinfocenter.client;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by joseph on 15/7/10.
 */

public class LimitQueue<E> implements Queue<E>
{
    private int limit;

    private Queue<E> queue = new LinkedList<E>();

    public LimitQueue(int limit)
    {
        this.limit = limit;
    }

    @Override
    public boolean offer(E e)
    {
        synchronized (queue)
        {
            if (queue.size() < limit)
            {
                queue.poll();
            }
            return queue.offer(e);
        }
    }

    @Override
    public E poll()
    {
        synchronized (queue)
        {
            return queue.poll();
        }
    }

    public int getLimit()
    {
        return limit;
    }

    @Override
    public boolean add(E e)
    {
        synchronized (queue)
        {
            return queue.add(e);
        }
    }

    @Override
    public E element()
    {
        synchronized (queue)
        {
            return queue.element();
        }
    }

    @Override
    public E peek()
    {
        synchronized (queue)
        {
            return queue.peek();
        }
    }

    @Override
    public boolean isEmpty()
    {
        synchronized (queue)
        {
            return queue.size() == 0 ? true : false;
        }
    }

    @Override
    public int size()
    {
        synchronized (queue)
        {
            return queue.size();
        }
    }

    @Override
    public E remove()
    {
        synchronized (queue)
        {
            return queue.remove();
        }
    }

    @Override
    public boolean addAll(Collection<? extends E> c)
    {
        synchronized (queue)
        {
            return queue.addAll(c);
        }
    }

    @Override
    public void clear()
    {
        synchronized (queue)
        {
            queue.clear();
        }
    }

    @Override
    public boolean contains(Object o)
    {
        synchronized (queue)
        {
            return queue.contains(o);
        }
    }

    @Override
    public boolean containsAll(Collection<?> c)
    {
        synchronized (queue)
        {
            return queue.containsAll(c);
        }
    }

    @Override
    public Iterator<E> iterator()
    {
        synchronized (queue)
        {
            return queue.iterator();
        }
    }

    @Override
    public boolean remove(Object o)
    {
        synchronized (queue)
        {
            return queue.remove(o);
        }
    }

    @Override
    public boolean removeAll(Collection<?> c)
    {
        synchronized (queue)
        {
            return queue.removeAll(c);
        }
    }

    @Override
    public boolean retainAll(Collection<?> c)
    {
        synchronized (queue)
        {
            return queue.retainAll(c);
        }
    }

    @Override
    public Object[] toArray()
    {
        synchronized (queue)
        {
            return queue.toArray();
        }
    }

    @Override
    public <T> T[] toArray(T[] a)
    {
        synchronized (queue)
        {
            return queue.toArray(a);
        }
    }
}

