#!/bin/bash
ps aux | grep jar | awk '{print $2}' | xargs kill

