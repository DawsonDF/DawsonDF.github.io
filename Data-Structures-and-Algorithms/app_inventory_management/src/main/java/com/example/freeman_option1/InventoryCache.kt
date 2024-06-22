package com.example.freeman_option1

/**
 * Inventory cache
 *
 * @param K
 * @param V
 * @property capacity
 * @constructor Create empty Inventory cache
 */
class InventoryCache<K, V>(private val capacity: Int) {
    private val map: LinkedHashMap<K, V> = object : LinkedHashMap<K, V>(capacity, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
            return size > capacity
        }
    }

    /**
     * Get
     *
     * @param key
     * @return
     */
    fun get(key: K): V? {
        synchronized(map) {
            return map[key]
        }
    }

    /**
     * Put
     *
     * @param key
     * @param value
     */
    fun put(key: K, value: V) {
        synchronized(map) {
            map[key] = value
        }
    }

    /**
     * Remove
     *
     * @param key
     * @return
     */
    fun remove(key: K): V? {
        synchronized(map) {
            return map.remove(key)
        }
    }

    /**
     * Clear
     *
     */
    fun clear() {
        synchronized(map) {
            map.clear()
        }
    }

    /**
     * Size
     *
     * @return
     */
    fun size(): Int {
        synchronized(map) {
            return map.size
        }
    }

    /**
     * Is empty
     *
     * @return
     */
    fun isEmpty(): Boolean {
        synchronized(map) {
            return map.isEmpty()
        }
    }

    /**
     * Contains key
     *
     * @param key
     * @return
     */
    fun containsKey(key: K): Boolean {
        synchronized(map) {
            return map.containsKey(key)
        }
    }

    /**
     * Contains value
     *
     * @param value
     * @return
     */
    fun containsValue(value: V): Boolean {
        synchronized(map) {
            return map.containsValue(value)
        }
    }

    override fun toString(): String {
        synchronized(map) {
            return map.toString()
        }
    }
}