package edu.umkc.cjk8zb.singlepass;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

import java.io.IOException;

public class MatrixMultiplication {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        Path input = new Path(args[0]);
        Path output = new Path(args[1]);

        // Create configuration
        Configuration conf = new Configuration(true);

        // Create job
        Job job = new Job(conf, "MultiplyMatrix");
        job.setJarByClass(MatrixMultiplication.class);

        // Setup MapReduce
        job.setMapperClass(SinglePassMapper.class);
        job.setReducerClass(SinglePassReducer.class);
        job.setNumReduceTasks(1);

        // Specify key / value
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        // Input
        FileInputFormat.addInputPath(job, input);
        job.setInputFormatClass(TextInputFormat.class);

        // Output
        FileOutputFormat.setOutputPath(job, output);
        job.setOutputFormatClass(TextOutputFormat.class);

        // Delete output if exists
        FileSystem fileSystem = FileSystem.get(conf);
        if (fileSystem.exists(output)) {
            fileSystem.delete(output, true);
        }

        System.exit(job.waitForCompletion(true) ? 0 : 1);
    }

}
